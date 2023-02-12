import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RecursoComponent } from '../list/recurso.component';
import { RecursoDetailComponent } from '../detail/recurso-detail.component';
import { RecursoUpdateComponent } from '../update/recurso-update.component';
import { RecursoRoutingResolveService } from './recurso-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const recursoRoute: Routes = [
  {
    path: '',
    component: RecursoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RecursoDetailComponent,
    resolve: {
      recurso: RecursoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RecursoUpdateComponent,
    resolve: {
      recurso: RecursoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RecursoUpdateComponent,
    resolve: {
      recurso: RecursoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(recursoRoute)],
  exports: [RouterModule],
})
export class RecursoRoutingModule {}
