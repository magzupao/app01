import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DialogoFormService } from './dialogo-form.service';
import { DialogoService } from '../service/dialogo.service';
import { IDialogo } from '../dialogo.model';
import { IConversacion } from 'app/entities/conversacion/conversacion.model';
import { ConversacionService } from 'app/entities/conversacion/service/conversacion.service';
import { IParticipante } from 'app/entities/participante/participante.model';
import { ParticipanteService } from 'app/entities/participante/service/participante.service';

import { DialogoUpdateComponent } from './dialogo-update.component';

describe('Dialogo Management Update Component', () => {
  let comp: DialogoUpdateComponent;
  let fixture: ComponentFixture<DialogoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dialogoFormService: DialogoFormService;
  let dialogoService: DialogoService;
  let conversacionService: ConversacionService;
  let participanteService: ParticipanteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DialogoUpdateComponent],
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
      .overrideTemplate(DialogoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DialogoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dialogoFormService = TestBed.inject(DialogoFormService);
    dialogoService = TestBed.inject(DialogoService);
    conversacionService = TestBed.inject(ConversacionService);
    participanteService = TestBed.inject(ParticipanteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Conversacion query and add missing value', () => {
      const dialogo: IDialogo = { id: 456 };
      const conversacion: IConversacion = { id: 46121 };
      dialogo.conversacion = conversacion;

      const conversacionCollection: IConversacion[] = [{ id: 8955 }];
      jest.spyOn(conversacionService, 'query').mockReturnValue(of(new HttpResponse({ body: conversacionCollection })));
      const additionalConversacions = [conversacion];
      const expectedCollection: IConversacion[] = [...additionalConversacions, ...conversacionCollection];
      jest.spyOn(conversacionService, 'addConversacionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dialogo });
      comp.ngOnInit();

      expect(conversacionService.query).toHaveBeenCalled();
      expect(conversacionService.addConversacionToCollectionIfMissing).toHaveBeenCalledWith(
        conversacionCollection,
        ...additionalConversacions.map(expect.objectContaining)
      );
      expect(comp.conversacionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Participante query and add missing value', () => {
      const dialogo: IDialogo = { id: 456 };
      const participante: IParticipante = { id: 8604 };
      dialogo.participante = participante;

      const participanteCollection: IParticipante[] = [{ id: 5033 }];
      jest.spyOn(participanteService, 'query').mockReturnValue(of(new HttpResponse({ body: participanteCollection })));
      const additionalParticipantes = [participante];
      const expectedCollection: IParticipante[] = [...additionalParticipantes, ...participanteCollection];
      jest.spyOn(participanteService, 'addParticipanteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dialogo });
      comp.ngOnInit();

      expect(participanteService.query).toHaveBeenCalled();
      expect(participanteService.addParticipanteToCollectionIfMissing).toHaveBeenCalledWith(
        participanteCollection,
        ...additionalParticipantes.map(expect.objectContaining)
      );
      expect(comp.participantesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const dialogo: IDialogo = { id: 456 };
      const conversacion: IConversacion = { id: 89985 };
      dialogo.conversacion = conversacion;
      const participante: IParticipante = { id: 47680 };
      dialogo.participante = participante;

      activatedRoute.data = of({ dialogo });
      comp.ngOnInit();

      expect(comp.conversacionsSharedCollection).toContain(conversacion);
      expect(comp.participantesSharedCollection).toContain(participante);
      expect(comp.dialogo).toEqual(dialogo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDialogo>>();
      const dialogo = { id: 123 };
      jest.spyOn(dialogoFormService, 'getDialogo').mockReturnValue(dialogo);
      jest.spyOn(dialogoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dialogo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dialogo }));
      saveSubject.complete();

      // THEN
      expect(dialogoFormService.getDialogo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dialogoService.update).toHaveBeenCalledWith(expect.objectContaining(dialogo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDialogo>>();
      const dialogo = { id: 123 };
      jest.spyOn(dialogoFormService, 'getDialogo').mockReturnValue({ id: null });
      jest.spyOn(dialogoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dialogo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dialogo }));
      saveSubject.complete();

      // THEN
      expect(dialogoFormService.getDialogo).toHaveBeenCalled();
      expect(dialogoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDialogo>>();
      const dialogo = { id: 123 };
      jest.spyOn(dialogoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dialogo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dialogoService.update).toHaveBeenCalled();
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

    describe('compareParticipante', () => {
      it('Should forward to participanteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(participanteService, 'compareParticipante');
        comp.compareParticipante(entity, entity2);
        expect(participanteService.compareParticipante).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
