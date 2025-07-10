export interface Product {
  id: number;
  name: string;
  category: string;
  price: number;
  quantity: number;
  isLowStock: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface ProductFormData {
  name: string;
  category: string;
  price: number;
  quantity: number;
}

export interface CsvImportResult {
  successCount: number;
  errorCount: number;
  errors: string[];
} 