import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConversacion } from '../conversacion.model';
import { ConversacionService } from '../service/conversacion.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './conversacion-delete-dialog.component.html',
})
export class ConversacionDeleteDialogComponent {
  conversacion?: IConversacion;

  constructor(protected conversacionService: ConversacionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.conversacionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
