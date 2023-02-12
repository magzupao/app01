import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRecurso, NewRecurso } from '../recurso.model';

export type PartialUpdateRecurso = Partial<IRecurso> & Pick<IRecurso, 'id'>;

export type EntityResponseType = HttpResponse<IRecurso>;
export type EntityArrayResponseType = HttpResponse<IRecurso[]>;

@Injectable({ providedIn: 'root' })
export class RecursoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/recursos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(recurso: NewRecurso): Observable<EntityResponseType> {
    return this.http.post<IRecurso>(this.resourceUrl, recurso, { observe: 'response' });
  }

  update(recurso: IRecurso): Observable<EntityResponseType> {
    return this.http.put<IRecurso>(`${this.resourceUrl}/${this.getRecursoIdentifier(recurso)}`, recurso, { observe: 'response' });
  }

  partialUpdate(recurso: PartialUpdateRecurso): Observable<EntityResponseType> {
    return this.http.patch<IRecurso>(`${this.resourceUrl}/${this.getRecursoIdentifier(recurso)}`, recurso, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRecurso>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRecurso[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRecursoIdentifier(recurso: Pick<IRecurso, 'id'>): number {
    return recurso.id;
  }

  compareRecurso(o1: Pick<IRecurso, 'id'> | null, o2: Pick<IRecurso, 'id'> | null): boolean {
    return o1 && o2 ? this.getRecursoIdentifier(o1) === this.getRecursoIdentifier(o2) : o1 === o2;
  }

  addRecursoToCollectionIfMissing<Type extends Pick<IRecurso, 'id'>>(
    recursoCollection: Type[],
    ...recursosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const recursos: Type[] = recursosToCheck.filter(isPresent);
    if (recursos.length > 0) {
      const recursoCollectionIdentifiers = recursoCollection.map(recursoItem => this.getRecursoIdentifier(recursoItem)!);
      const recursosToAdd = recursos.filter(recursoItem => {
        const recursoIdentifier = this.getRecursoIdentifier(recursoItem);
        if (recursoCollectionIdentifiers.includes(recursoIdentifier)) {
          return false;
        }
        recursoCollectionIdentifiers.push(recursoIdentifier);
        return true;
      });
      return [...recursosToAdd, ...recursoCollection];
    }
    return recursoCollection;
  }
}
