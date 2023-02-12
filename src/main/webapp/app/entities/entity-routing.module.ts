import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'conversacion',
        data: { pageTitle: 'app01App.conversacion.home.title' },
        loadChildren: () => import('./conversacion/conversacion.module').then(m => m.ConversacionModule),
      },
      {
        path: 'participante',
        data: { pageTitle: 'app01App.participante.home.title' },
        loadChildren: () => import('./participante/participante.module').then(m => m.ParticipanteModule),
      },
      {
        path: 'dialogo',
        data: { pageTitle: 'app01App.dialogo.home.title' },
        loadChildren: () => import('./dialogo/dialogo.module').then(m => m.DialogoModule),
      },
      {
        path: 'recurso',
        data: { pageTitle: 'app01App.recurso.home.title' },
        loadChildren: () => import('./recurso/recurso.module').then(m => m.RecursoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
