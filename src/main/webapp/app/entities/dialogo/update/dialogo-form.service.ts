import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDialogo, NewDialogo } from '../dialogo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDialogo for edit and NewDialogoFormGroupInput for create.
 */
type DialogoFormGroupInput = IDialogo | PartialWithRequiredKeyOf<NewDialogo>;

type DialogoFormDefaults = Pick<NewDialogo, 'id'>;

type DialogoFormGroupContent = {
  id: FormControl<IDialogo['id'] | NewDialogo['id']>;
  mensaje: FormControl<IDialogo['mensaje']>;
  conversacion: FormControl<IDialogo['conversacion']>;
  participante: FormControl<IDialogo['participante']>;
};

export type DialogoFormGroup = FormGroup<DialogoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DialogoFormService {
  createDialogoFormGroup(dialogo: DialogoFormGroupInput = { id: null }): DialogoFormGroup {
    const dialogoRawValue = {
      ...this.getFormDefaults(),
      ...dialogo,
    };
    return new FormGroup<DialogoFormGroupContent>({
      id: new FormControl(
        { value: dialogoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      mensaje: new FormControl(dialogoRawValue.mensaje),
      conversacion: new FormControl(dialogoRawValue.conversacion),
      participante: new FormControl(dialogoRawValue.participante),
    });
  }

  getDialogo(form: DialogoFormGroup): IDialogo | NewDialogo {
    return form.getRawValue() as IDialogo | NewDialogo;
  }

  resetForm(form: DialogoFormGroup, dialogo: DialogoFormGroupInput): void {
    const dialogoRawValue = { ...this.getFormDefaults(), ...dialogo };
    form.reset(
      {
        ...dialogoRawValue,
        id: { value: dialogoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DialogoFormDefaults {
    return {
      id: null,
    };
  }
}
