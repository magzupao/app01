import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDialogo } from '../dialogo.model';
import { DialogoService } from '../service/dialogo.service';

@Injectable({ providedIn: 'root' })
export class DialogoRoutingResolveService implements Resolve<IDialogo | null> {
  constructor(protected service: DialogoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDialogo | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dialogo: HttpResponse<IDialogo>) => {
          if (dialogo.body) {
            return of(dialogo.body);
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
