import axios from 'axios';
import { Product, ProductFormData, CsvImportResult, Order, OrderDto } from '../types/Product.ts';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const productApi = {
  getAllProducts: async (): Promise<Product[]> => {
    const response = await api.get('/products');
    return response.data;
  },
  getProductById: async (id: number): Promise<Product> => {
    const response = await api.get(`/products/${id}`);
    return response.data;
  },
  createProduct: async (productData: ProductFormData): Promise<Product> => {
    const response = await api.post('/products', productData);
    return response.data;
  },
  updateProduct: async (id: number, productData: ProductFormData): Promise<Product> => {
    const response = await api.put(`/products/${id}`, productData);
    return response.data;
  },
  deleteProduct: async (id: number): Promise<void> => {
    await api.delete(`/products/${id}`);
  },
  getLowStockProducts: async (): Promise<Product[]> => {
    const response = await api.get('/products/low-stock');
    return response.data;
  },
  searchProductsByName: async (name: string): Promise<Product[]> => {
    const response = await api.get(`/products/search?name=${encodeURIComponent(name)}`);
    return response.data;
  },
  getProductsByCategory: async (category: string): Promise<Product[]> => {
    const response = await api.get(`/products/category/${encodeURIComponent(category)}`);
    return response.data;
  },
  importProductsFromCsv: async (file: File): Promise<CsvImportResult> => {
    const formData = new FormData();
    formData.append('file', file);
    const response = await api.post('/products/import', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return response.data;
  },
  getOperationCounter: async (): Promise<number> => {
    const response = await api.get('/products/stats/operations');
    return response.data;
  },
  // Order APIs
  processOrder: async (orderData: OrderDto): Promise<Order> => {
    const response = await api.post('/orders', orderData);
    return response.data;
  },
  getAllOrders: async (): Promise<Order[]> => {
    const response = await api.get('/orders');
    return response.data;
  },
  getOrderById: async (id: number): Promise<Order> => {
    const response = await api.get(`/orders/${id}`);
    return response.data;
  },
};

export default api; 