import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConversacion, NewConversacion } from '../conversacion.model';

export type PartialUpdateConversacion = Partial<IConversacion> & Pick<IConversacion, 'id'>;

export type EntityResponseType = HttpResponse<IConversacion>;
export type EntityArrayResponseType = HttpResponse<IConversacion[]>;

@Injectable({ providedIn: 'root' })
export class ConversacionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/conversacions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(conversacion: NewConversacion): Observable<EntityResponseType> {
    return this.http.post<IConversacion>(this.resourceUrl, conversacion, { observe: 'response' });
  }

  update(conversacion: IConversacion): Observable<EntityResponseType> {
    return this.http.put<IConversacion>(`${this.resourceUrl}/${this.getConversacionIdentifier(conversacion)}`, conversacion, {
      observe: 'response',
    });
  }

  partialUpdate(conversacion: PartialUpdateConversacion): Observable<EntityResponseType> {
    return this.http.patch<IConversacion>(`${this.resourceUrl}/${this.getConversacionIdentifier(conversacion)}`, conversacion, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConversacion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConversacion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConversacionIdentifier(conversacion: Pick<IConversacion, 'id'>): number {
    return conversacion.id;
  }

  compareConversacion(o1: Pick<IConversacion, 'id'> | null, o2: Pick<IConversacion, 'id'> | null): boolean {
    return o1 && o2 ? this.getConversacionIdentifier(o1) === this.getConversacionIdentifier(o2) : o1 === o2;
  }

  addConversacionToCollectionIfMissing<Type extends Pick<IConversacion, 'id'>>(
    conversacionCollection: Type[],
    ...conversacionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const conversacions: Type[] = conversacionsToCheck.filter(isPresent);
    if (conversacions.length > 0) {
      const conversacionCollectionIdentifiers = conversacionCollection.map(
        conversacionItem => this.getConversacionIdentifier(conversacionItem)!
      );
      const conversacionsToAdd = conversacions.filter(conversacionItem => {
        const conversacionIdentifier = this.getConversacionIdentifier(conversacionItem);
        if (conversacionCollectionIdentifiers.includes(conversacionIdentifier)) {
          return false;
        }
        conversacionCollectionIdentifiers.push(conversacionIdentifier);
        return true;
      });
      return [...conversacionsToAdd, ...conversacionCollection];
    }
    return conversacionCollection;
  }
}
