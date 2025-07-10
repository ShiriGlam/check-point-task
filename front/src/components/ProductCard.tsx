import React from 'react';
import { Product } from '../types/Product.tsx';
import './ProductCard.css';

interface ProductCardProps {
  product: Product;
  onEdit: (product: Product) => void;
  onDelete: (id: number) => void;
}

const ProductCard: React.FC<ProductCardProps> = ({ product, onEdit, onDelete }) => {
  const isLowStock = product.quantity < 5;

  return (
    <div className={`product-card${isLowStock ? ' low-stock' : ''}`}>
      <div className="card-actions">
        <button onClick={() => onEdit(product)} title="ערוך מוצר">ערוך</button>
        <button onClick={() => onDelete(product.id)} title="מחק מוצר">מחק</button>
      </div>
      <div className="product-title">{product.name}</div>
      <div className="product-category">קטגוריה: {product.category}</div>
      <div className="product-details">
        <div className="product-price">₪{product.price.toFixed(2)}</div>
        <div className="product-quantity">כמות: {product.quantity}</div>
      </div>
      {isLowStock && (
        <div className="low-stock-alert">כמות נמוכה במלאי - יש צורך בהזמנה</div>
      )}
      <div className="product-dates">
        <div>נוצר: {new Date(product.createdAt).toLocaleDateString('he-IL')}</div>
        <div>עודכן: {new Date(product.updatedAt).toLocaleDateString('he-IL')}</div>
      </div>
    </div>
  );
};

export default ProductCard; 