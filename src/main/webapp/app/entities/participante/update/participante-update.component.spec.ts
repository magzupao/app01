import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ParticipanteFormService } from './participante-form.service';
import { ParticipanteService } from '../service/participante.service';
import { IParticipante } from '../participante.model';
import { IConversacion } from 'app/entities/conversacion/conversacion.model';
import { ConversacionService } from 'app/entities/conversacion/service/conversacion.service';

import { ParticipanteUpdateComponent } from './participante-update.component';

describe('Participante Management Update Component', () => {
  let comp: ParticipanteUpdateComponent;
  let fixture: ComponentFixture<ParticipanteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let participanteFormService: ParticipanteFormService;
  let participanteService: ParticipanteService;
  let conversacionService: ConversacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ParticipanteUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ParticipanteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParticipanteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    participanteFormService = TestBed.inject(ParticipanteFormService);
    participanteService = TestBed.inject(ParticipanteService);
    conversacionService = TestBed.inject(ConversacionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Conversacion query and add missing value', () => {
      const participante: IParticipante = { id: 456 };
      const conversacion: IConversacion = { id: 47573 };
      participante.conversacion = conversacion;

      const conversacionCollection: IConversacion[] = [{ id: 28106 }];
      jest.spyOn(conversacionService, 'query').mockReturnValue(of(new HttpResponse({ body: conversacionCollection })));
      const additionalConversacions = [conversacion];
      const expectedCollection: IConversacion[] = [...additionalConversacions, ...conversacionCollection];
      jest.spyOn(conversacionService, 'addConversacionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ participante });
      comp.ngOnInit();

      expect(conversacionService.query).toHaveBeenCalled();
      expect(conversacionService.addConversacionToCollectionIfMissing).toHaveBeenCalledWith(
        conversacionCollection,
        ...additionalConversacions.map(expect.objectContaining)
      );
      expect(comp.conversacionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const participante: IParticipante = { id: 456 };
      const conversacion: IConversacion = { id: 8566 };
      participante.conversacion = conversacion;

      activatedRoute.data = of({ participante });
      comp.ngOnInit();

      expect(comp.conversacionsSharedCollection).toContain(conversacion);
      expect(comp.participante).toEqual(participante);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParticipante>>();
      const participante = { id: 123 };
      jest.spyOn(participanteFormService, 'getParticipante').mockReturnValue(participante);
      jest.spyOn(participanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ participante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: participante }));
      saveSubject.complete();

      // THEN
      expect(participanteFormService.getParticipante).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(participanteService.update).toHaveBeenCalledWith(expect.objectContaining(participante));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParticipante>>();
      const participante = { id: 123 };
      jest.spyOn(participanteFormService, 'getParticipante').mockReturnValue({ id: null });
      jest.spyOn(participanteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ participante: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: participante }));
      saveSubject.complete();

      // THEN
      expect(participanteFormService.getParticipante).toHaveBeenCalled();
      expect(participanteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParticipante>>();
      const participante = { id: 123 };
      jest.spyOn(participanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ participante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(participanteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareConversacion', () => {
      it('Should forward to conversacionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(conversacionService, 'compareConversacion');
        comp.compareConversacion(entity, entity2);
        expect(conversacionService.compareConversacion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
