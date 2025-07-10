import React, { useRef, useState } from 'react';
import { productApi } from '../services/api.ts';
import { CsvImportResult } from '../types/Product.tsx';
import './CsvImport.css';

interface CsvImportProps {
  onImport: () => void;
}

const CsvImport: React.FC<CsvImportProps> = ({ onImport }) => {
  const fileInput = useRef<HTMLInputElement>(null);
  const [result, setResult] = useState<CsvImportResult | null>(null);
  const [error, setError] = useState<string | null>(null);

  const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    setError(null);
    setResult(null);
    const file = e.target.files?.[0];
    if (!file) return;
    try {
      const res = await productApi.importProductsFromCsv(file);
      setResult(res);
      onImport();
    } catch (err) {
      setError('Error importing file');
    }
  };

  return (
    <div className="csv-import">
      <input type="file" accept=".csv" ref={fileInput} onChange={handleFileChange} />
      {result && (
        <div className="import-result">
          Import finished: {result.successCount} successes, {result.errorCount} errors
          {result.errors.length > 0 && (
            <ul>
              {result.errors.map((err, i) => <li key={i}>{err}</li>)}
            </ul>
          )}
        </div>
      )}
      {error && <div className="import-error">{error}</div>}
    </div>
  );
};

export default CsvImport; 