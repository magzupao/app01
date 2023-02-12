import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ConversacionFormService, ConversacionFormGroup } from './conversacion-form.service';
import { IConversacion } from '../conversacion.model';
import { ConversacionService } from '../service/conversacion.service';

@Component({
  selector: 'jhi-conversacion-update',
  templateUrl: './conversacion-update.component.html',
})
export class ConversacionUpdateComponent implements OnInit {
  isSaving = false;
  conversacion: IConversacion | null = null;

  editForm: ConversacionFormGroup = this.conversacionFormService.createConversacionFormGroup();

  constructor(
    protected conversacionService: ConversacionService,
    protected conversacionFormService: ConversacionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conversacion }) => {
      this.conversacion = conversacion;
      if (conversacion) {
        this.updateForm(conversacion);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const conversacion = this.conversacionFormService.getConversacion(this.editForm);
    if (conversacion.id !== null) {
      this.subscribeToSaveResponse(this.conversacionService.update(conversacion));
    } else {
      this.subscribeToSaveResponse(this.conversacionService.create(conversacion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConversacion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(conversacion: IConversacion): void {
    this.conversacion = conversacion;
    this.conversacionFormService.resetForm(this.editForm, conversacion);
  }
}
