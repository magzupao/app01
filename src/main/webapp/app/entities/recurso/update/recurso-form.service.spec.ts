import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../recurso.test-samples';

import { RecursoFormService } from './recurso-form.service';

describe('Recurso Form Service', () => {
  let service: RecursoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RecursoFormService);
  });

  describe('Service methods', () => {
    describe('createRecursoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRecursoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fichero: expect.any(Object),
            conversacion: expect.any(Object),
            participante: expect.any(Object),
          })
        );
      });

      it('passing IRecurso should create a new form with FormGroup', () => {
        const formGroup = service.createRecursoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fichero: expect.any(Object),
            conversacion: expect.any(Object),
            participante: expect.any(Object),
          })
        );
      });
    });

    describe('getRecurso', () => {
      it('should return NewRecurso for default Recurso initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRecursoFormGroup(sampleWithNewData);

        const recurso = service.getRecurso(formGroup) as any;

        expect(recurso).toMatchObject(sampleWithNewData);
      });

      it('should return NewRecurso for empty Recurso initial value', () => {
        const formGroup = service.createRecursoFormGroup();

        const recurso = service.getRecurso(formGroup) as any;

        expect(recurso).toMatchObject({});
      });

      it('should return IRecurso', () => {
        const formGroup = service.createRecursoFormGroup(sampleWithRequiredData);

        const recurso = service.getRecurso(formGroup) as any;

        expect(recurso).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRecurso should not enable id FormControl', () => {
        const formGroup = service.createRecursoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRecurso should disable id FormControl', () => {
        const formGroup = service.createRecursoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
