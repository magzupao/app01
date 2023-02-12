import { IConversacion } from 'app/entities/conversacion/conversacion.model';
import { IParticipante } from 'app/entities/participante/participante.model';

export interface IDialogo {
  id: number;
  mensaje?: string | null;
  conversacion?: Pick<IConversacion, 'id'> | null;
  participante?: Pick<IParticipante, 'id'> | null;
}

export type NewDialogo = Omit<IDialogo, 'id'> & { id: null };
