import { IConversacion } from 'app/entities/conversacion/conversacion.model';
import { IParticipante } from 'app/entities/participante/participante.model';

export interface IRecurso {
  id: number;
  fichero?: string | null;
  conversacion?: Pick<IConversacion, 'id'> | null;
  participante?: Pick<IParticipante, 'id'> | null;
}

export type NewRecurso = Omit<IRecurso, 'id'> & { id: null };
