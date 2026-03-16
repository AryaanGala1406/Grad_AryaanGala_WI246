export interface Student {
  regNo: number;
  rollNo: number;
  name: string;
  standard: number;
  school: string;
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  percentage: number;
}

export interface StudentCreate {
  rollNo: number;
  name: string;
  standard: number;
  school: string;
  gender: 'MALE' | 'FEMALE' | 'OTHER';
  percentage: number;
}

export type StudentUpdate = Partial<StudentCreate>;