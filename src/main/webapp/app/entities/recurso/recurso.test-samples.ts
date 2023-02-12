import { IRecurso, NewRecurso } from './recurso.model';

export const sampleWithRequiredData: IRecurso = {
  id: 76726,
};

export const sampleWithPartialData: IRecurso = {
  id: 20080,
};

export const sampleWithFullData: IRecurso = {
  id: 18814,
  fichero: 'Incredible Quetzal Taiwan',
};

export const sampleWithNewData: NewRecurso = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
