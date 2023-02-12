import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParticipante, NewParticipante } from '../participante.model';

export type PartialUpdateParticipante = Partial<IParticipante> & Pick<IParticipante, 'id'>;

export type EntityResponseType = HttpResponse<IParticipante>;
export type EntityArrayResponseType = HttpResponse<IParticipante[]>;

@Injectable({ providedIn: 'root' })
export class ParticipanteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/participantes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(participante: NewParticipante): Observable<EntityResponseType> {
    return this.http.post<IParticipante>(this.resourceUrl, participante, { observe: 'response' });
  }

  update(participante: IParticipante): Observable<EntityResponseType> {
    return this.http.put<IParticipante>(`${this.resourceUrl}/${this.getParticipanteIdentifier(participante)}`, participante, {
      observe: 'response',
    });
  }

  partialUpdate(participante: PartialUpdateParticipante): Observable<EntityResponseType> {
    return this.http.patch<IParticipante>(`${this.resourceUrl}/${this.getParticipanteIdentifier(participante)}`, participante, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IParticipante>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParticipante[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getParticipanteIdentifier(participante: Pick<IParticipante, 'id'>): number {
    return participante.id;
  }

  compareParticipante(o1: Pick<IParticipante, 'id'> | null, o2: Pick<IParticipante, 'id'> | null): boolean {
    return o1 && o2 ? this.getParticipanteIdentifier(o1) === this.getParticipanteIdentifier(o2) : o1 === o2;
  }

  addParticipanteToCollectionIfMissing<Type extends Pick<IParticipante, 'id'>>(
    participanteCollection: Type[],
    ...participantesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const participantes: Type[] = participantesToCheck.filter(isPresent);
    if (participantes.length > 0) {
      const participanteCollectionIdentifiers = participanteCollection.map(
        participanteItem => this.getParticipanteIdentifier(participanteItem)!
      );
      const participantesToAdd = participantes.filter(participanteItem => {
        const participanteIdentifier = this.getParticipanteIdentifier(participanteItem);
        if (participanteCollectionIdentifiers.includes(participanteIdentifier)) {
          return false;
        }
        participanteCollectionIdentifiers.push(participanteIdentifier);
        return true;
      });
      return [...participantesToAdd, ...participanteCollection];
    }
    return participanteCollection;
  }
}
