import { IConversacion } from 'app/entities/conversacion/conversacion.model';

export interface IParticipante {
  id: number;
  email?: string | null;
  nombre?: string | null;
  conversacion?: Pick<IConversacion, 'id'> | null;
}

export type NewParticipante = Omit<IParticipante, 'id'> & { id: null };
