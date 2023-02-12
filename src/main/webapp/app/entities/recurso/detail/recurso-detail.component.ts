import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRecurso } from '../recurso.model';

@Component({
  selector: 'jhi-recurso-detail',
  templateUrl: './recurso-detail.component.html',
})
export class RecursoDetailComponent implements OnInit {
  recurso: IRecurso | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recurso }) => {
      this.recurso = recurso;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
