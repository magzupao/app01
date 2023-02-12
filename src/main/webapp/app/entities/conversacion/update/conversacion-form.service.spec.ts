import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../conversacion.test-samples';

import { ConversacionFormService } from './conversacion-form.service';

describe('Conversacion Form Service', () => {
  let service: ConversacionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConversacionFormService);
  });

  describe('Service methods', () => {
    describe('createConversacionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createConversacionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titulo: expect.any(Object),
          })
        );
      });

      it('passing IConversacion should create a new form with FormGroup', () => {
        const formGroup = service.createConversacionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titulo: expect.any(Object),
          })
        );
      });
    });

    describe('getConversacion', () => {
      it('should return NewConversacion for default Conversacion initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createConversacionFormGroup(sampleWithNewData);

        const conversacion = service.getConversacion(formGroup) as any;

        expect(conversacion).toMatchObject(sampleWithNewData);
      });

      it('should return NewConversacion for empty Conversacion initial value', () => {
        const formGroup = service.createConversacionFormGroup();

        const conversacion = service.getConversacion(formGroup) as any;

        expect(conversacion).toMatchObject({});
      });

      it('should return IConversacion', () => {
        const formGroup = service.createConversacionFormGroup(sampleWithRequiredData);

        const conversacion = service.getConversacion(formGroup) as any;

        expect(conversacion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConversacion should not enable id FormControl', () => {
        const formGroup = service.createConversacionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConversacion should disable id FormControl', () => {
        const formGroup = service.createConversacionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
