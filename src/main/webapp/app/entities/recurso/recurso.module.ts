import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RecursoComponent } from './list/recurso.component';
import { RecursoDetailComponent } from './detail/recurso-detail.component';
import { RecursoUpdateComponent } from './update/recurso-update.component';
import { RecursoDeleteDialogComponent } from './delete/recurso-delete-dialog.component';
import { RecursoRoutingModule } from './route/recurso-routing.module';

@NgModule({
  imports: [SharedModule, RecursoRoutingModule],
  declarations: [RecursoComponent, RecursoDetailComponent, RecursoUpdateComponent, RecursoDeleteDialogComponent],
})
export class RecursoModule {}
