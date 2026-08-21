export default function Pagination({ page, totalPages, onPageChange }) {
  if (totalPages <= 1) return null

  return (
    <nav className="pagination" aria-label="Student list pages">
      <button onClick={() => onPageChange(page - 1)} disabled={page === 0}>Previous</button>
      <span>Page {page + 1} of {totalPages}</span>
      <button onClick={() => onPageChange(page + 1)} disabled={page + 1 >= totalPages}>Next</button>
    </nav>
  )
}
