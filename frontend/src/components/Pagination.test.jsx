import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { expect, test, vi } from 'vitest'
import Pagination from './Pagination'

test('moves to the next page and disables previous on the first page', async () => {
  const onPageChange = vi.fn()
  const user = userEvent.setup()

  render(<Pagination page={0} totalPages={3} onPageChange={onPageChange} />)

  expect(screen.getByRole('button', { name: 'Previous' })).toBeDisabled()
  await user.click(screen.getByRole('button', { name: 'Next' }))
  expect(onPageChange).toHaveBeenCalledWith(1)
})
