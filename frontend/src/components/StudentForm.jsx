import {useEffect, useState} from 'react'

const emptyStudent = {
    studentCode: '', fullName: '', email: '', phone: '', dateOfBirth: '', address: ''
}

export default function StudentForm({student, onSubmit, onCancel, saving}) {
    const [form, setForm] = useState(emptyStudent)

    useEffect(() => {
        setForm(student ? {...emptyStudent, ...student, dateOfBirth: student.dateOfBirth || ''} : emptyStudent)
    }, [student])

    const updateField = (event) => setForm((current) => ({...current, [event.target.name]: event.target.value}))

    const submit = (event) => {
        event.preventDefault()
        onSubmit({
            ...form, phone: form.phone || null, dateOfBirth: form.dateOfBirth || null, address: form.address || null
        })
    }

    return (<form className="student-form" onSubmit={submit}>
            <h2>{student ? 'Edit student' : 'Add student'}</h2>
            <label>Student code<input name="studentCode" value={form.studentCode} onChange={updateField} required
                                      maxLength="30"/></label>
            <label>Full name<input name="fullName" value={form.fullName} onChange={updateField} required
                                   maxLength="100"/></label>
            <label>Email<input name="email" type="email" value={form.email} onChange={updateField} required
                               maxLength="254"/></label>
            <label>Phone<input name="phone" value={form.phone} onChange={updateField} maxLength="30"/></label>
            <label>Date of birth<input name="dateOfBirth" type="date" value={form.dateOfBirth} onChange={updateField}/></label>
            <label>Address<textarea name="address" value={form.address} onChange={updateField} maxLength="500"
                                    rows="3"/></label>
            <div className="form-actions">
                <button type="submit" disabled={saving}>{saving ? 'Saving…' : 'Save'}</button>
                <button type="button" className="secondary" onClick={onCancel} disabled={saving}>Cancel</button>
            </div>
        </form>)
}
