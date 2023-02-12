import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ConversacionFormService } from './conversacion-form.service';
import { ConversacionService } from '../service/conversacion.service';
import { IConversacion } from '../conversacion.model';

import { ConversacionUpdateComponent } from './conversacion-update.component';

describe('Conversacion Management Update Component', () => {
  let comp: ConversacionUpdateComponent;
  let fixture: ComponentFixture<ConversacionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let conversacionFormService: ConversacionFormService;
  let conversacionService: ConversacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ConversacionUpdateComponent],
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
      .overrideTemplate(ConversacionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConversacionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    conversacionFormService = TestBed.inject(ConversacionFormService);
    conversacionService = TestBed.inject(ConversacionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const conversacion: IConversacion = { id: 456 };

      activatedRoute.data = of({ conversacion });
      comp.ngOnInit();

      expect(comp.conversacion).toEqual(conversacion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConversacion>>();
      const conversacion = { id: 123 };
      jest.spyOn(conversacionFormService, 'getConversacion').mockReturnValue(conversacion);
      jest.spyOn(conversacionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conversacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conversacion }));
      saveSubject.complete();

      // THEN
      expect(conversacionFormService.getConversacion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(conversacionService.update).toHaveBeenCalledWith(expect.objectContaining(conversacion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConversacion>>();
      const conversacion = { id: 123 };
      jest.spyOn(conversacionFormService, 'getConversacion').mockReturnValue({ id: null });
      jest.spyOn(conversacionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conversacion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conversacion }));
      saveSubject.complete();

      // THEN
      expect(conversacionFormService.getConversacion).toHaveBeenCalled();
      expect(conversacionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConversacion>>();
      const conversacion = { id: 123 };
      jest.spyOn(conversacionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conversacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(conversacionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
