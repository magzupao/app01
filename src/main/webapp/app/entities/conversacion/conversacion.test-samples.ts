import { IConversacion, NewConversacion } from './conversacion.model';

export const sampleWithRequiredData: IConversacion = {
  id: 45973,
};

export const sampleWithPartialData: IConversacion = {
  id: 3924,
};

export const sampleWithFullData: IConversacion = {
  id: 30822,
  titulo: 'info-mediaries',
};

export const sampleWithNewData: NewConversacion = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
