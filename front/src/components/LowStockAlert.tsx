import React, { useEffect, useState } from 'react';
import { productApi } from '../services/api.ts';
import { Product } from '../types/Product.tsx';
import './LowStockAlert.css';

const LowStockAlert: React.FC = () => {
  const [lowStock, setLowStock] = useState<Product[]>([]);

  useEffect(() => {
    productApi.getLowStockProducts().then(setLowStock);
  }, []);

  if (lowStock.length === 0) return null;

  return (
    <div className="low-stock-alert-bar">
      <strong>אזהרה:</strong> יש {lowStock.length} מוצרים עם מלאי נמוך!
      <ul>
        {lowStock.map(p => (
          <li key={p.id}>{p.name} ({p.quantity})</li>
        ))}
      </ul>
    </div>
  );
};

export default LowStockAlert; 