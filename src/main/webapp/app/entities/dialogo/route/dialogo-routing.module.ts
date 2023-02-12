import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DialogoComponent } from '../list/dialogo.component';
import { DialogoDetailComponent } from '../detail/dialogo-detail.component';
import { DialogoUpdateComponent } from '../update/dialogo-update.component';
import { DialogoRoutingResolveService } from './dialogo-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const dialogoRoute: Routes = [
  {
    path: '',
    component: DialogoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DialogoDetailComponent,
    resolve: {
      dialogo: DialogoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DialogoUpdateComponent,
    resolve: {
      dialogo: DialogoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DialogoUpdateComponent,
    resolve: {
      dialogo: DialogoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dialogoRoute)],
  exports: [RouterModule],
})
export class DialogoRoutingModule {}
