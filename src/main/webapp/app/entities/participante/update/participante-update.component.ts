import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ParticipanteFormService, ParticipanteFormGroup } from './participante-form.service';
import { IParticipante } from '../participante.model';
import { ParticipanteService } from '../service/participante.service';
import { IConversacion } from 'app/entities/conversacion/conversacion.model';
import { ConversacionService } from 'app/entities/conversacion/service/conversacion.service';

@Component({
  selector: 'jhi-participante-update',
  templateUrl: './participante-update.component.html',
})
export class ParticipanteUpdateComponent implements OnInit {
  isSaving = false;
  participante: IParticipante | null = null;

  conversacionsSharedCollection: IConversacion[] = [];

  editForm: ParticipanteFormGroup = this.participanteFormService.createParticipanteFormGroup();

  constructor(
    protected participanteService: ParticipanteService,
    protected participanteFormService: ParticipanteFormService,
    protected conversacionService: ConversacionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareConversacion = (o1: IConversacion | null, o2: IConversacion | null): boolean =>
    this.conversacionService.compareConversacion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ participante }) => {
      this.participante = participante;
      if (participante) {
        this.updateForm(participante);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const participante = this.participanteFormService.getParticipante(this.editForm);
    if (participante.id !== null) {
      this.subscribeToSaveResponse(this.participanteService.update(participante));
    } else {
      this.subscribeToSaveResponse(this.participanteService.create(participante));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParticipante>>): void {
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

  protected updateForm(participante: IParticipante): void {
    this.participante = participante;
    this.participanteFormService.resetForm(this.editForm, participante);

    this.conversacionsSharedCollection = this.conversacionService.addConversacionToCollectionIfMissing<IConversacion>(
      this.conversacionsSharedCollection,
      participante.conversacion
    );
  }

  protected loadRelationshipsOptions(): void {
    this.conversacionService
      .query()
      .pipe(map((res: HttpResponse<IConversacion[]>) => res.body ?? []))
      .pipe(
        map((conversacions: IConversacion[]) =>
          this.conversacionService.addConversacionToCollectionIfMissing<IConversacion>(conversacions, this.participante?.conversacion)
        )
      )
      .subscribe((conversacions: IConversacion[]) => (this.conversacionsSharedCollection = conversacions));
  }
}
