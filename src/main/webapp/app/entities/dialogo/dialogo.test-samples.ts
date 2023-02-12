import { IDialogo, NewDialogo } from './dialogo.model';

export const sampleWithRequiredData: IDialogo = {
  id: 73941,
};

export const sampleWithPartialData: IDialogo = {
  id: 93274,
  mensaje: 'leverage connecting',
};

export const sampleWithFullData: IDialogo = {
  id: 8584,
  mensaje: 'THX Central Fresh',
};

export const sampleWithNewData: NewDialogo = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
