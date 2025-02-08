export interface IData {
  id: string;
  key: string;
  value: string;
  fid: string;
}

export interface IDataCollection {
  data: IData[];
}

export interface IDataCollectionResponse {
  code: number;
  message: string;
  selected: string;
  data: IDataCollection;
}
