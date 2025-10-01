export interface Organization {
  id?: number;
  ownerId?: number;
  name: string;
  coordinatesId: number;
  creationDate: string;
  officialAddressId?: number;
  annualTurnover: number;
  employeesCount: number;
  rating: number;
  type?: string;
  postalAddressId?: number;
}
