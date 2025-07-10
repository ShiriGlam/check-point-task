import React, { useState } from 'react';
import { Product, ProductFormData } from '../types/Product.tsx';
import { productApi } from '../services/api.ts';
import './ProductForm.css';

interface ProductFormProps {
  product?: Product | null;
  onSubmit: () => void;
  onCancel: () => void;
}

const initialForm = (product?: Product | null): ProductFormData =>
  product
    ? {
        name: product.name,
        category: product.category,
        price: product.price,
        quantity: product.quantity,
      }
    : {
        name: '',
        category: '',
        price: 0,
        quantity: 0,
      };

const ProductForm: React.FC<ProductFormProps> = ({ product, onSubmit, onCancel }) => {
  const [form, setForm] = useState<ProductFormData>(initialForm(product));
  const [error, setError] = useState<string | null>(null);
  const isEdit = !!product;

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm(f => ({ ...f, [name]: name === 'price' || name === 'quantity' ? Number(value) : value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    try {
      if (isEdit && product) {
        await productApi.updateProduct(product.id, form);
      } else {
        await productApi.createProduct(form);
      }
      onSubmit();
    } catch (err: any) {
      setError('שגיאה בשמירה');
    }
  };

  return (
    <form className="product-form" onSubmit={handleSubmit}>
      <h2>{isEdit ? 'עריכת מוצר' : 'הוספת מוצר'}</h2>
      <div>
        <label>שם:</label>
        <input name="name" value={form.name} onChange={handleChange} required />
      </div>
      <div>
        <label>קטגוריה:</label>
        <input name="category" value={form.category} onChange={handleChange} required />
      </div>
      <div>
        <label>מחיר:</label>
        <input name="price" type="number" value={form.price} onChange={handleChange} min={0} step={0.01} required />
      </div>
      <div>
        <label>כמות:</label>
        <input name="quantity" type="number" value={form.quantity} onChange={handleChange} min={0} required />
      </div>
      {error && <div className="error">{error}</div>}
      <div className="form-actions">
        <button type="submit">{isEdit ? 'עדכן' : 'הוסף'}</button>
        <button type="button" onClick={onCancel}>ביטול</button>
      </div>
    </form>
  );
};

export default ProductForm; 