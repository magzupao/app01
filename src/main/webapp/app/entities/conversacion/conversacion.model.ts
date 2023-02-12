export interface IConversacion {
  id: number;
  titulo?: string | null;
}

export type NewConversacion = Omit<IConversacion, 'id'> & { id: null };
