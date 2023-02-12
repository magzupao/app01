import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RecursoFormService } from './recurso-form.service';
import { RecursoService } from '../service/recurso.service';
import { IRecurso } from '../recurso.model';
import { IConversacion } from 'app/entities/conversacion/conversacion.model';
import { ConversacionService } from 'app/entities/conversacion/service/conversacion.service';
import { IParticipante } from 'app/entities/participante/participante.model';
import { ParticipanteService } from 'app/entities/participante/service/participante.service';

import { RecursoUpdateComponent } from './recurso-update.component';

describe('Recurso Management Update Component', () => {
  let comp: RecursoUpdateComponent;
  let fixture: ComponentFixture<RecursoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let recursoFormService: RecursoFormService;
  let recursoService: RecursoService;
  let conversacionService: ConversacionService;
  let participanteService: ParticipanteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RecursoUpdateComponent],
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
      .overrideTemplate(RecursoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RecursoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    recursoFormService = TestBed.inject(RecursoFormService);
    recursoService = TestBed.inject(RecursoService);
    conversacionService = TestBed.inject(ConversacionService);
    participanteService = TestBed.inject(ParticipanteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Conversacion query and add missing value', () => {
      const recurso: IRecurso = { id: 456 };
      const conversacion: IConversacion = { id: 98151 };
      recurso.conversacion = conversacion;

      const conversacionCollection: IConversacion[] = [{ id: 78485 }];
      jest.spyOn(conversacionService, 'query').mockReturnValue(of(new HttpResponse({ body: conversacionCollection })));
      const additionalConversacions = [conversacion];
      const expectedCollection: IConversacion[] = [...additionalConversacions, ...conversacionCollection];
      jest.spyOn(conversacionService, 'addConversacionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recurso });
      comp.ngOnInit();

      expect(conversacionService.query).toHaveBeenCalled();
      expect(conversacionService.addConversacionToCollectionIfMissing).toHaveBeenCalledWith(
        conversacionCollection,
        ...additionalConversacions.map(expect.objectContaining)
      );
      expect(comp.conversacionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Participante query and add missing value', () => {
      const recurso: IRecurso = { id: 456 };
      const participante: IParticipante = { id: 40507 };
      recurso.participante = participante;

      const participanteCollection: IParticipante[] = [{ id: 80242 }];
      jest.spyOn(participanteService, 'query').mockReturnValue(of(new HttpResponse({ body: participanteCollection })));
      const additionalParticipantes = [participante];
      const expectedCollection: IParticipante[] = [...additionalParticipantes, ...participanteCollection];
      jest.spyOn(participanteService, 'addParticipanteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recurso });
      comp.ngOnInit();

      expect(participanteService.query).toHaveBeenCalled();
      expect(participanteService.addParticipanteToCollectionIfMissing).toHaveBeenCalledWith(
        participanteCollection,
        ...additionalParticipantes.map(expect.objectContaining)
      );
      expect(comp.participantesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const recurso: IRecurso = { id: 456 };
      const conversacion: IConversacion = { id: 24431 };
      recurso.conversacion = conversacion;
      const participante: IParticipante = { id: 12677 };
      recurso.participante = participante;

      activatedRoute.data = of({ recurso });
      comp.ngOnInit();

      expect(comp.conversacionsSharedCollection).toContain(conversacion);
      expect(comp.participantesSharedCollection).toContain(participante);
      expect(comp.recurso).toEqual(recurso);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecurso>>();
      const recurso = { id: 123 };
      jest.spyOn(recursoFormService, 'getRecurso').mockReturnValue(recurso);
      jest.spyOn(recursoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recurso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recurso }));
      saveSubject.complete();

      // THEN
      expect(recursoFormService.getRecurso).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(recursoService.update).toHaveBeenCalledWith(expect.objectContaining(recurso));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecurso>>();
      const recurso = { id: 123 };
      jest.spyOn(recursoFormService, 'getRecurso').mockReturnValue({ id: null });
      jest.spyOn(recursoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recurso: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recurso }));
      saveSubject.complete();

      // THEN
      expect(recursoFormService.getRecurso).toHaveBeenCalled();
      expect(recursoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecurso>>();
      const recurso = { id: 123 };
      jest.spyOn(recursoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recurso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(recursoService.update).toHaveBeenCalled();
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
