import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DialogoFormService, DialogoFormGroup } from './dialogo-form.service';
import { IDialogo } from '../dialogo.model';
import { DialogoService } from '../service/dialogo.service';
import { IConversacion } from 'app/entities/conversacion/conversacion.model';
import { ConversacionService } from 'app/entities/conversacion/service/conversacion.service';
import { IParticipante } from 'app/entities/participante/participante.model';
import { ParticipanteService } from 'app/entities/participante/service/participante.service';

@Component({
  selector: 'jhi-dialogo-update',
  templateUrl: './dialogo-update.component.html',
})
export class DialogoUpdateComponent implements OnInit {
  isSaving = false;
  dialogo: IDialogo | null = null;

  conversacionsSharedCollection: IConversacion[] = [];
  participantesSharedCollection: IParticipante[] = [];

  editForm: DialogoFormGroup = this.dialogoFormService.createDialogoFormGroup();

  constructor(
    protected dialogoService: DialogoService,
    protected dialogoFormService: DialogoFormService,
    protected conversacionService: ConversacionService,
    protected participanteService: ParticipanteService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareConversacion = (o1: IConversacion | null, o2: IConversacion | null): boolean =>
    this.conversacionService.compareConversacion(o1, o2);

  compareParticipante = (o1: IParticipante | null, o2: IParticipante | null): boolean =>
    this.participanteService.compareParticipante(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dialogo }) => {
      this.dialogo = dialogo;
      if (dialogo) {
        this.updateForm(dialogo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dialogo = this.dialogoFormService.getDialogo(this.editForm);
    if (dialogo.id !== null) {
      this.subscribeToSaveResponse(this.dialogoService.update(dialogo));
    } else {
      this.subscribeToSaveResponse(this.dialogoService.create(dialogo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDialogo>>): void {
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

  protected updateForm(dialogo: IDialogo): void {
    this.dialogo = dialogo;
    this.dialogoFormService.resetForm(this.editForm, dialogo);

    this.conversacionsSharedCollection = this.conversacionService.addConversacionToCollectionIfMissing<IConversacion>(
      this.conversacionsSharedCollection,
      dialogo.conversacion
    );
    this.participantesSharedCollection = this.participanteService.addParticipanteToCollectionIfMissing<IParticipante>(
      this.participantesSharedCollection,
      dialogo.participante
    );
  }

  protected loadRelationshipsOptions(): void {
    this.conversacionService
      .query()
      .pipe(map((res: HttpResponse<IConversacion[]>) => res.body ?? []))
      .pipe(
        map((conversacions: IConversacion[]) =>
          this.conversacionService.addConversacionToCollectionIfMissing<IConversacion>(conversacions, this.dialogo?.conversacion)
        )
      )
      .subscribe((conversacions: IConversacion[]) => (this.conversacionsSharedCollection = conversacions));

    this.participanteService
      .query()
      .pipe(map((res: HttpResponse<IParticipante[]>) => res.body ?? []))
      .pipe(
        map((participantes: IParticipante[]) =>
          this.participanteService.addParticipanteToCollectionIfMissing<IParticipante>(participantes, this.dialogo?.participante)
        )
      )
      .subscribe((participantes: IParticipante[]) => (this.participantesSharedCollection = participantes));
  }
}
