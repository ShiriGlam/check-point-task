import React from 'react';
import ProductList from './components/ProductList.tsx';

function App() {
  return (
    <div style={{ maxWidth: 900, margin: '0 auto', padding: 32, background: '#181a20', minHeight: '100vh' }}>
      <h1 style={{ textAlign: 'center', color: '#a3a8f0', marginBottom: 32 }}>ניהול מוצרים</h1>
      <ProductList />
    </div>
  );
}

export default App; 