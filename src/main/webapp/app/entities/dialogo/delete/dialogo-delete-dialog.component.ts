import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDialogo } from '../dialogo.model';
import { DialogoService } from '../service/dialogo.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './dialogo-delete-dialog.component.html',
})
export class DialogoDeleteDialogComponent {
  dialogo?: IDialogo;

  constructor(protected dialogoService: DialogoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dialogoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
