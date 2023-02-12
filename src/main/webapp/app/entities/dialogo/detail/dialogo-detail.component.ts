import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDialogo } from '../dialogo.model';

@Component({
  selector: 'jhi-dialogo-detail',
  templateUrl: './dialogo-detail.component.html',
})
export class DialogoDetailComponent implements OnInit {
  dialogo: IDialogo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dialogo }) => {
      this.dialogo = dialogo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
