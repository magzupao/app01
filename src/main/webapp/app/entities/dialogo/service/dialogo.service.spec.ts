import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDialogo } from '../dialogo.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../dialogo.test-samples';

import { DialogoService } from './dialogo.service';

const requireRestSample: IDialogo = {
  ...sampleWithRequiredData,
};

describe('Dialogo Service', () => {
  let service: DialogoService;
  let httpMock: HttpTestingController;
  let expectedResult: IDialogo | IDialogo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DialogoService);
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

    it('should create a Dialogo', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const dialogo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(dialogo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Dialogo', () => {
      const dialogo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(dialogo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Dialogo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Dialogo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Dialogo', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDialogoToCollectionIfMissing', () => {
      it('should add a Dialogo to an empty array', () => {
        const dialogo: IDialogo = sampleWithRequiredData;
        expectedResult = service.addDialogoToCollectionIfMissing([], dialogo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dialogo);
      });

      it('should not add a Dialogo to an array that contains it', () => {
        const dialogo: IDialogo = sampleWithRequiredData;
        const dialogoCollection: IDialogo[] = [
          {
            ...dialogo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDialogoToCollectionIfMissing(dialogoCollection, dialogo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Dialogo to an array that doesn't contain it", () => {
        const dialogo: IDialogo = sampleWithRequiredData;
        const dialogoCollection: IDialogo[] = [sampleWithPartialData];
        expectedResult = service.addDialogoToCollectionIfMissing(dialogoCollection, dialogo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dialogo);
      });

      it('should add only unique Dialogo to an array', () => {
        const dialogoArray: IDialogo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const dialogoCollection: IDialogo[] = [sampleWithRequiredData];
        expectedResult = service.addDialogoToCollectionIfMissing(dialogoCollection, ...dialogoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dialogo: IDialogo = sampleWithRequiredData;
        const dialogo2: IDialogo = sampleWithPartialData;
        expectedResult = service.addDialogoToCollectionIfMissing([], dialogo, dialogo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dialogo);
        expect(expectedResult).toContain(dialogo2);
      });

      it('should accept null and undefined values', () => {
        const dialogo: IDialogo = sampleWithRequiredData;
        expectedResult = service.addDialogoToCollectionIfMissing([], null, dialogo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dialogo);
      });

      it('should return initial array if no Dialogo is added', () => {
        const dialogoCollection: IDialogo[] = [sampleWithRequiredData];
        expectedResult = service.addDialogoToCollectionIfMissing(dialogoCollection, undefined, null);
        expect(expectedResult).toEqual(dialogoCollection);
      });
    });

    describe('compareDialogo', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDialogo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDialogo(entity1, entity2);
        const compareResult2 = service.compareDialogo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDialogo(entity1, entity2);
        const compareResult2 = service.compareDialogo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDialogo(entity1, entity2);
        const compareResult2 = service.compareDialogo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
