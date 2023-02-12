import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConversacion } from '../conversacion.model';
import { ConversacionService } from '../service/conversacion.service';

@Injectable({ providedIn: 'root' })
export class ConversacionRoutingResolveService implements Resolve<IConversacion | null> {
  constructor(protected service: ConversacionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConversacion | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((conversacion: HttpResponse<IConversacion>) => {
          if (conversacion.body) {
            return of(conversacion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
