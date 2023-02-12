import { IParticipante, NewParticipante } from './participante.model';

export const sampleWithRequiredData: IParticipante = {
  id: 67295,
};

export const sampleWithPartialData: IParticipante = {
  id: 32330,
  nombre: 'interface Directives functionalities',
};

export const sampleWithFullData: IParticipante = {
  id: 61628,
  email: 'Bernardo.Weissnat@gmail.com',
  nombre: 'Iraq blue',
};

export const sampleWithNewData: NewParticipante = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
