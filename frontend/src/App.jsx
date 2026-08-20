import { useCallback, useEffect, useState } from 'react'
import StudentForm from './components/StudentForm'
import Pagination from './components/Pagination'
import { createStudent, deleteStudent, getStudents, searchStudents, updateStudent } from './api/students'

const initialPage = { content: [], page: 0, size: 10, totalElements: 0, totalPages: 0 }

export default function App() {
  const [studentPage, setStudentPage] = useState(initialPage)
  const [searchTerm, setSearchTerm] = useState('')
  const [activeSearch, setActiveSearch] = useState('')
  const [editingStudent, setEditingStudent] = useState(null)
  const [showForm, setShowForm] = useState(false)
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState('')

  const loadStudents = useCallback(async (page = 0, search = activeSearch) => {
    setLoading(true)
    setError('')
    try {
      const params = { page, size: 10, sort: 'fullName,asc' }
      const response = search.trim() ? await searchStudents(search.trim(), params) : await getStudents(params)
      setStudentPage(response.data)
    } catch (requestError) {
      setError(requestError.response?.data?.message || 'Unable to load students. Please try again.')
    } finally {
      setLoading(false)
    }
  }, [activeSearch])

  useEffect(() => { loadStudents() }, [loadStudents])

  const submitSearch = (event) => {
    event.preventDefault()
    setActiveSearch(searchTerm)
    loadStudents(0, searchTerm)
  }

  const submitForm = async (student) => {
    setSaving(true)
    setError('')
    try {
      if (editingStudent) await updateStudent(editingStudent.id, student)
      else await createStudent(student)
      setShowForm(false)
      setEditingStudent(null)
      loadStudents(editingStudent ? studentPage.page : 0)
    } catch (requestError) {
      setError(requestError.response?.data?.message || 'Unable to save the student.')
    } finally {
      setSaving(false)
    }
  }

  const removeStudent = async (student) => {
    if (!window.confirm(`Delete ${student.fullName}?`)) return
    setError('')
    try {
      await deleteStudent(student.id)
      const page = studentPage.content.length === 1 && studentPage.page > 0 ? studentPage.page - 1 : studentPage.page
      loadStudents(page)
    } catch (requestError) {
      setError(requestError.response?.data?.message || 'Unable to delete the student.')
    }
  }

  const startAdd = () => { setEditingStudent(null); setShowForm(true) }
  const startEdit = (student) => { setEditingStudent(student); setShowForm(true) }
  const cancelForm = () => { setEditingStudent(null); setShowForm(false) }

  return (
    <main className="app-shell">
      <header>
        <div><h1>Student Management</h1><p>Administration portal</p></div>
        <button onClick={startAdd}>Add student</button>
      </header>

      <form className="search" onSubmit={submitSearch}>
        <label htmlFor="search">Search by name</label>
        <input id="search" value={searchTerm} onChange={(event) => setSearchTerm(event.target.value)} placeholder="e.g. Ada Lovelace" />
        <button type="submit" className="secondary">Search</button>
      </form>

      {error && <p className="error" role="alert">{error}</p>}
      {showForm && <StudentForm student={editingStudent} onSubmit={submitForm} onCancel={cancelForm} saving={saving} />}

      <section className="student-list" aria-live="polite">
        <div className="list-heading"><h2>Students</h2><span>{studentPage.totalElements} total</span></div>
        {loading ? <p>Loading students…</p> : studentPage.content.length === 0 ? <p>No students found.</p> : (
          <div className="table-wrap"><table><thead><tr><th>Code</th><th>Name</th><th>Email</th><th>Phone</th><th>Actions</th></tr></thead>
            <tbody>{studentPage.content.map((student) => <tr key={student.id}><td>{student.studentCode}</td><td>{student.fullName}</td><td>{student.email}</td><td>{student.phone || '—'}</td><td className="actions"><button className="link-button" onClick={() => startEdit(student)}>Edit</button><button className="link-button danger" onClick={() => removeStudent(student)}>Delete</button></td></tr>)}</tbody>
          </table></div>
        )}
        <Pagination page={studentPage.page} totalPages={studentPage.totalPages} onPageChange={(page) => loadStudents(page)} />
      </section>
    </main>
  )
}
