import React from 'react';
import { Product } from '../types/Product.ts';
import './ProductCard.css';

interface ProductCardProps {
  product: Product;
  onEdit: (product: Product) => void;
  onDelete: (id: number) => void;
}

const ProductCard: React.FC<ProductCardProps> = ({ product, onEdit, onDelete }) => {
  return (
    <div className={`product-card${product.isLowStock ? ' low-stock' : ''}`}>
      <div className="card-actions">
        <button onClick={() => onEdit(product)} title="Edit product">Edit</button>
        <button onClick={() => onDelete(product.id)} title="Delete product">Delete</button>
      </div>
      <div className="product-title">{product.name}</div>
      <div className="product-category">Category: {product.category}</div>
      <div className="product-details">
        <div className="product-price">${product.price.toFixed(2)}</div>
        <div className="product-quantity">Quantity: {product.quantity}</div>
      </div>
      {product.isLowStock && (
        <div className="low-stock-alert">Low stock - reorder needed</div>
      )}
      <div className="product-dates">
        <div>Created: {new Date(product.createdAt).toLocaleDateString('en-US')}</div>
        <div>Updated: {new Date(product.updatedAt).toLocaleDateString('en-US')}</div>
      </div>
    </div>
  );
};

export default ProductCard; 