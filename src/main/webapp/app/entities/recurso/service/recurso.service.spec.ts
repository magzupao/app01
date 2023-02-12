import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRecurso } from '../recurso.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../recurso.test-samples';

import { RecursoService } from './recurso.service';

const requireRestSample: IRecurso = {
  ...sampleWithRequiredData,
};

describe('Recurso Service', () => {
  let service: RecursoService;
  let httpMock: HttpTestingController;
  let expectedResult: IRecurso | IRecurso[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RecursoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Recurso', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const recurso = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(recurso).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Recurso', () => {
      const recurso = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(recurso).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Recurso', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Recurso', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Recurso', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRecursoToCollectionIfMissing', () => {
      it('should add a Recurso to an empty array', () => {
        const recurso: IRecurso = sampleWithRequiredData;
        expectedResult = service.addRecursoToCollectionIfMissing([], recurso);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recurso);
      });

      it('should not add a Recurso to an array that contains it', () => {
        const recurso: IRecurso = sampleWithRequiredData;
        const recursoCollection: IRecurso[] = [
          {
            ...recurso,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRecursoToCollectionIfMissing(recursoCollection, recurso);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Recurso to an array that doesn't contain it", () => {
        const recurso: IRecurso = sampleWithRequiredData;
        const recursoCollection: IRecurso[] = [sampleWithPartialData];
        expectedResult = service.addRecursoToCollectionIfMissing(recursoCollection, recurso);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recurso);
      });

      it('should add only unique Recurso to an array', () => {
        const recursoArray: IRecurso[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const recursoCollection: IRecurso[] = [sampleWithRequiredData];
        expectedResult = service.addRecursoToCollectionIfMissing(recursoCollection, ...recursoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const recurso: IRecurso = sampleWithRequiredData;
        const recurso2: IRecurso = sampleWithPartialData;
        expectedResult = service.addRecursoToCollectionIfMissing([], recurso, recurso2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recurso);
        expect(expectedResult).toContain(recurso2);
      });

      it('should accept null and undefined values', () => {
        const recurso: IRecurso = sampleWithRequiredData;
        expectedResult = service.addRecursoToCollectionIfMissing([], null, recurso, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recurso);
      });

      it('should return initial array if no Recurso is added', () => {
        const recursoCollection: IRecurso[] = [sampleWithRequiredData];
        expectedResult = service.addRecursoToCollectionIfMissing(recursoCollection, undefined, null);
        expect(expectedResult).toEqual(recursoCollection);
      });
    });

    describe('compareRecurso', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRecurso(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRecurso(entity1, entity2);
        const compareResult2 = service.compareRecurso(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRecurso(entity1, entity2);
        const compareResult2 = service.compareRecurso(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRecurso(entity1, entity2);
        const compareResult2 = service.compareRecurso(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
