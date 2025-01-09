export interface IData {
  id: string;
  key: string;
  value: string;
}

export interface IDataCollection {
  data: IData[];
}

export interface IDataCollectionResponse {
  code: number;
  message: string;
  data: IDataCollection;
}
