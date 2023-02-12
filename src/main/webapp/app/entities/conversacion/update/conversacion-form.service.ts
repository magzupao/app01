import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IConversacion, NewConversacion } from '../conversacion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConversacion for edit and NewConversacionFormGroupInput for create.
 */
type ConversacionFormGroupInput = IConversacion | PartialWithRequiredKeyOf<NewConversacion>;

type ConversacionFormDefaults = Pick<NewConversacion, 'id'>;

type ConversacionFormGroupContent = {
  id: FormControl<IConversacion['id'] | NewConversacion['id']>;
  titulo: FormControl<IConversacion['titulo']>;
};

export type ConversacionFormGroup = FormGroup<ConversacionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConversacionFormService {
  createConversacionFormGroup(conversacion: ConversacionFormGroupInput = { id: null }): ConversacionFormGroup {
    const conversacionRawValue = {
      ...this.getFormDefaults(),
      ...conversacion,
    };
    return new FormGroup<ConversacionFormGroupContent>({
      id: new FormControl(
        { value: conversacionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      titulo: new FormControl(conversacionRawValue.titulo),
    });
  }

  getConversacion(form: ConversacionFormGroup): IConversacion | NewConversacion {
    return form.getRawValue() as IConversacion | NewConversacion;
  }

  resetForm(form: ConversacionFormGroup, conversacion: ConversacionFormGroupInput): void {
    const conversacionRawValue = { ...this.getFormDefaults(), ...conversacion };
    form.reset(
      {
        ...conversacionRawValue,
        id: { value: conversacionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ConversacionFormDefaults {
    return {
      id: null,
    };
  }
}
