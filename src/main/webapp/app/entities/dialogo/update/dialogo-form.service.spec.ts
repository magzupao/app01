import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../dialogo.test-samples';

import { DialogoFormService } from './dialogo-form.service';

describe('Dialogo Form Service', () => {
  let service: DialogoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DialogoFormService);
  });

  describe('Service methods', () => {
    describe('createDialogoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDialogoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mensaje: expect.any(Object),
            conversacion: expect.any(Object),
            participante: expect.any(Object),
          })
        );
      });

      it('passing IDialogo should create a new form with FormGroup', () => {
        const formGroup = service.createDialogoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mensaje: expect.any(Object),
            conversacion: expect.any(Object),
            participante: expect.any(Object),
          })
        );
      });
    });

    describe('getDialogo', () => {
      it('should return NewDialogo for default Dialogo initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDialogoFormGroup(sampleWithNewData);

        const dialogo = service.getDialogo(formGroup) as any;

        expect(dialogo).toMatchObject(sampleWithNewData);
      });

      it('should return NewDialogo for empty Dialogo initial value', () => {
        const formGroup = service.createDialogoFormGroup();

        const dialogo = service.getDialogo(formGroup) as any;

        expect(dialogo).toMatchObject({});
      });

      it('should return IDialogo', () => {
        const formGroup = service.createDialogoFormGroup(sampleWithRequiredData);

        const dialogo = service.getDialogo(formGroup) as any;

        expect(dialogo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDialogo should not enable id FormControl', () => {
        const formGroup = service.createDialogoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDialogo should disable id FormControl', () => {
        const formGroup = service.createDialogoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
