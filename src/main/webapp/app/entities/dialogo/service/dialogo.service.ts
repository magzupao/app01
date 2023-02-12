import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDialogo, NewDialogo } from '../dialogo.model';

export type PartialUpdateDialogo = Partial<IDialogo> & Pick<IDialogo, 'id'>;

export type EntityResponseType = HttpResponse<IDialogo>;
export type EntityArrayResponseType = HttpResponse<IDialogo[]>;

@Injectable({ providedIn: 'root' })
export class DialogoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dialogos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dialogo: NewDialogo): Observable<EntityResponseType> {
    return this.http.post<IDialogo>(this.resourceUrl, dialogo, { observe: 'response' });
  }

  update(dialogo: IDialogo): Observable<EntityResponseType> {
    return this.http.put<IDialogo>(`${this.resourceUrl}/${this.getDialogoIdentifier(dialogo)}`, dialogo, { observe: 'response' });
  }

  partialUpdate(dialogo: PartialUpdateDialogo): Observable<EntityResponseType> {
    return this.http.patch<IDialogo>(`${this.resourceUrl}/${this.getDialogoIdentifier(dialogo)}`, dialogo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDialogo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDialogo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDialogoIdentifier(dialogo: Pick<IDialogo, 'id'>): number {
    return dialogo.id;
  }

  compareDialogo(o1: Pick<IDialogo, 'id'> | null, o2: Pick<IDialogo, 'id'> | null): boolean {
    return o1 && o2 ? this.getDialogoIdentifier(o1) === this.getDialogoIdentifier(o2) : o1 === o2;
  }

  addDialogoToCollectionIfMissing<Type extends Pick<IDialogo, 'id'>>(
    dialogoCollection: Type[],
    ...dialogosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const dialogos: Type[] = dialogosToCheck.filter(isPresent);
    if (dialogos.length > 0) {
      const dialogoCollectionIdentifiers = dialogoCollection.map(dialogoItem => this.getDialogoIdentifier(dialogoItem)!);
      const dialogosToAdd = dialogos.filter(dialogoItem => {
        const dialogoIdentifier = this.getDialogoIdentifier(dialogoItem);
        if (dialogoCollectionIdentifiers.includes(dialogoIdentifier)) {
          return false;
        }
        dialogoCollectionIdentifiers.push(dialogoIdentifier);
        return true;
      });
      return [...dialogosToAdd, ...dialogoCollection];
    }
    return dialogoCollection;
  }
}
