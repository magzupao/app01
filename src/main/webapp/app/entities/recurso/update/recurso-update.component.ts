import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RecursoFormService, RecursoFormGroup } from './recurso-form.service';
import { IRecurso } from '../recurso.model';
import { RecursoService } from '../service/recurso.service';
import { IConversacion } from 'app/entities/conversacion/conversacion.model';
import { ConversacionService } from 'app/entities/conversacion/service/conversacion.service';
import { IParticipante } from 'app/entities/participante/participante.model';
import { ParticipanteService } from 'app/entities/participante/service/participante.service';

@Component({
  selector: 'jhi-recurso-update',
  templateUrl: './recurso-update.component.html',
})
export class RecursoUpdateComponent implements OnInit {
  isSaving = false;
  recurso: IRecurso | null = null;

  conversacionsSharedCollection: IConversacion[] = [];
  participantesSharedCollection: IParticipante[] = [];

  editForm: RecursoFormGroup = this.recursoFormService.createRecursoFormGroup();

  constructor(
    protected recursoService: RecursoService,
    protected recursoFormService: RecursoFormService,
    protected conversacionService: ConversacionService,
    protected participanteService: ParticipanteService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareConversacion = (o1: IConversacion | null, o2: IConversacion | null): boolean =>
    this.conversacionService.compareConversacion(o1, o2);

  compareParticipante = (o1: IParticipante | null, o2: IParticipante | null): boolean =>
    this.participanteService.compareParticipante(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recurso }) => {
      this.recurso = recurso;
      if (recurso) {
        this.updateForm(recurso);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const recurso = this.recursoFormService.getRecurso(this.editForm);
    if (recurso.id !== null) {
      this.subscribeToSaveResponse(this.recursoService.update(recurso));
    } else {
      this.subscribeToSaveResponse(this.recursoService.create(recurso));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecurso>>): void {
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

  protected updateForm(recurso: IRecurso): void {
    this.recurso = recurso;
    this.recursoFormService.resetForm(this.editForm, recurso);

    this.conversacionsSharedCollection = this.conversacionService.addConversacionToCollectionIfMissing<IConversacion>(
      this.conversacionsSharedCollection,
      recurso.conversacion
    );
    this.participantesSharedCollection = this.participanteService.addParticipanteToCollectionIfMissing<IParticipante>(
      this.participantesSharedCollection,
      recurso.participante
    );
  }

  protected loadRelationshipsOptions(): void {
    this.conversacionService
      .query()
      .pipe(map((res: HttpResponse<IConversacion[]>) => res.body ?? []))
      .pipe(
        map((conversacions: IConversacion[]) =>
          this.conversacionService.addConversacionToCollectionIfMissing<IConversacion>(conversacions, this.recurso?.conversacion)
        )
      )
      .subscribe((conversacions: IConversacion[]) => (this.conversacionsSharedCollection = conversacions));

    this.participanteService
      .query()
      .pipe(map((res: HttpResponse<IParticipante[]>) => res.body ?? []))
      .pipe(
        map((participantes: IParticipante[]) =>
          this.participanteService.addParticipanteToCollectionIfMissing<IParticipante>(participantes, this.recurso?.participante)
        )
      )
      .subscribe((participantes: IParticipante[]) => (this.participantesSharedCollection = participantes));
  }
}
