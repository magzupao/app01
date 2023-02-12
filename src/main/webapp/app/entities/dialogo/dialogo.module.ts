import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DialogoComponent } from './list/dialogo.component';
import { DialogoDetailComponent } from './detail/dialogo-detail.component';
import { DialogoUpdateComponent } from './update/dialogo-update.component';
import { DialogoDeleteDialogComponent } from './delete/dialogo-delete-dialog.component';
import { DialogoRoutingModule } from './route/dialogo-routing.module';

@NgModule({
  imports: [SharedModule, DialogoRoutingModule],
  declarations: [DialogoComponent, DialogoDetailComponent, DialogoUpdateComponent, DialogoDeleteDialogComponent],
})
export class DialogoModule {}
