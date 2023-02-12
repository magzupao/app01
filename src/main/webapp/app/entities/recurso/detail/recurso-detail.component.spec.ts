import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RecursoDetailComponent } from './recurso-detail.component';

describe('Recurso Management Detail Component', () => {
  let comp: RecursoDetailComponent;
  let fixture: ComponentFixture<RecursoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RecursoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ recurso: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RecursoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RecursoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load recurso on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.recurso).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
