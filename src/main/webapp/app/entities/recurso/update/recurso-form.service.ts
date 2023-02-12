import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRecurso, NewRecurso } from '../recurso.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRecurso for edit and NewRecursoFormGroupInput for create.
 */
type RecursoFormGroupInput = IRecurso | PartialWithRequiredKeyOf<NewRecurso>;

type RecursoFormDefaults = Pick<NewRecurso, 'id'>;

type RecursoFormGroupContent = {
  id: FormControl<IRecurso['id'] | NewRecurso['id']>;
  fichero: FormControl<IRecurso['fichero']>;
  conversacion: FormControl<IRecurso['conversacion']>;
  participante: FormControl<IRecurso['participante']>;
};

export type RecursoFormGroup = FormGroup<RecursoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RecursoFormService {
  createRecursoFormGroup(recurso: RecursoFormGroupInput = { id: null }): RecursoFormGroup {
    const recursoRawValue = {
      ...this.getFormDefaults(),
      ...recurso,
    };
    return new FormGroup<RecursoFormGroupContent>({
      id: new FormControl(
        { value: recursoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      fichero: new FormControl(recursoRawValue.fichero),
      conversacion: new FormControl(recursoRawValue.conversacion),
      participante: new FormControl(recursoRawValue.participante),
    });
  }

  getRecurso(form: RecursoFormGroup): IRecurso | NewRecurso {
    return form.getRawValue() as IRecurso | NewRecurso;
  }

  resetForm(form: RecursoFormGroup, recurso: RecursoFormGroupInput): void {
    const recursoRawValue = { ...this.getFormDefaults(), ...recurso };
    form.reset(
      {
        ...recursoRawValue,
        id: { value: recursoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RecursoFormDefaults {
    return {
      id: null,
    };
  }
}
