import React, { useEffect, useState } from 'react';
import { productApi } from '../services/api.ts';
import ProductCard from './ProductCard.tsx';
import LowStockAlert from './LowStockAlert.tsx';
import ProductForm from './ProductForm.tsx';
import CsvImport from './CsvImport.tsx';
import { Product } from '../types/Product.tsx';
import './ProductList.css';

const ProductList: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);
  const [showForm, setShowForm] = useState(false);

  const fetchProducts = () => {
    productApi.getAllProducts().then(setProducts);
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  const handleEdit = (product: Product) => {
    setEditingProduct(product);
    setShowForm(true);
  };

  const handleDelete = async (id: number) => {
    await productApi.deleteProduct(id);
    setProducts(products.filter(p => p.id !== id));
  };

  const handleAdd = () => {
    setEditingProduct(null);
    setShowForm(true);
  };

  const handleFormSubmit = () => {
    setShowForm(false);
    fetchProducts();
  };

  const handleOrderComplete = () => {
    fetchProducts(); 
  };

  return (
    <div className="product-list-container">
      <div className="product-list-header">
        <h1>Product Management</h1>
        <div className="product-list-actions">
          <button onClick={handleAdd}>Add Product</button>
          <CsvImport onImport={fetchProducts} />
        </div>
      </div>
      <LowStockAlert />
      {showForm && (
        <ProductForm
          product={editingProduct}
          onSubmit={handleFormSubmit}
          onCancel={() => setShowForm(false)}
        />
      )}
      <div className="product-list-grid">
        {products.map(product => (
          <ProductCard
            key={product.id}
            product={product}
            onEdit={handleEdit}
            onDelete={handleDelete}
            onOrderComplete={handleOrderComplete}
          />
        ))}
      </div>
    </div>
  );
};

export default ProductList; 